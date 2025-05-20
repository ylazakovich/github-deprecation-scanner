#!/bin/bash
set -e

log_file="$1"

if [[ -z "$log_file" ]]; then
  echo "❌ No log file provided."
  exit 1
fi

if [[ ! -f "$log_file" ]]; then
  echo "❌ Log file '$log_file' does not exist."
  exit 1
fi

while IFS= read -r line; do
  if echo "$line" | grep -i "deprecated" > /dev/null; then
    echo "::warning file=$log_file::Deprecated usage: $line"
  fi
done < "$log_file"

exit 0