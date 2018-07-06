#!/bin/bash

set -euo pipefail

tar cfv .circleci/secrets.tar secrets
gpg -c --batch --yes --passphrase "$1" .circleci/secrets.tar
