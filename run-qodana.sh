#!/bin/bash

# Prompt for Qodana token if not set
if [ -z "$QODANA_TOKEN" ]; then
    read -p "Enter your Qodana token: " QODANA_TOKEN
fi

# Get current git branch
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)


# Run Qodana
docker run \
   -v $(pwd):/data/project/ \
   -e QODANA_TOKEN="$QODANA_TOKEN" \
   -e QODANA_REMOTE_URL="$(git config --get remote.origin.url)" \
   -e QODANA_BRANCH="$CURRENT_BRANCH" \
   -e QODANA_REVISION="$(git rev-parse HEAD)" \
   jetbrains/qodana-jvm

echo "Qodana analysis complete."