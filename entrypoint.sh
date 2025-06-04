#!/bin/bash
set -euo pipefail

COMMIT_SHA="${GITHUB_SHA:-$(git rev-parse HEAD)}"
OWNER_REPO="${GITHUB_REPOSITORY:-ylazakovich/github-deprecation-scanner}"

echo "🔍 Scanning logs for commit: $COMMIT_SHA in $OWNER_REPO"

RUN_ID=$(gh api \
  -H "Accept: application/vnd.github+json" \
  /repos/"$OWNER_REPO"/actions/runs \
  --paginate \
  | jq -r --arg sha "$COMMIT_SHA" '.workflow_runs[] | select(.head_sha == $sha) | .id' \
  | head -n1)

if [[ -z "$RUN_ID" ]]; then
  echo "❌ No workflow run found for commit $COMMIT_SHA"
  exit 1
fi

echo "✅ Found run ID: $RUN_ID. Downloading logs..."

mkdir -p /tmp/logs
gh run download "$RUN_ID" -D /tmp/logs

echo "📦 Extracting logs..."
find /tmp/logs -name '*.zip' -exec unzip -o {} -d /tmp/logs/extracted \;

echo "🔎 Scanning for 'deprecated' in logs..."

MATCHES=0
while IFS= read -r file; do
  while IFS= read -r line; do
    if echo "$line" | grep -i "deprecated" > /dev/null; then
      echo "::warning file=$file::Deprecated usage: $line"
      MATCHES=$((MATCHES + 1))
    fi
  done < "$file"
done < <(find /tmp/logs/extracted -type f)

if [[ "$MATCHES" -eq 0 ]]; then
  echo "✅ No deprecated usages found."
else
  echo "⚠️ Found $MATCHES deprecated usages."
fi

exit 0
