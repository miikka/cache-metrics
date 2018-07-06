#!/bin/bash

set -euo pipefail

gpg -d --batch --passphrase "$1" .circleci/secrets.tar.gpg > .circleci/secrets.tar
tar xfv .circleci/secrets.tar
