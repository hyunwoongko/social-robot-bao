#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
File: config.sample.py
Description: unittest configuration for Python SDK of the Cognitive Face API.
- Copy `config.sample.py` to `config.py`.
- Change the `BASE_URL` if necessary.
- Assign the `KEY` with a valid Subscription Key.
"""

# Subscription Key for calling the Cognitive Face API.
KEY = 'b6889d34f1184378bfc045cd6b8a92a9'

# Base URL for calling the Cognitive Face API.
# default is 'https://westus.api.cognitive.microsoft.com/face/v1.0/'
BASE_URL = 'https://koreacentral.api.cognitive.microsoft.com/face/v1.0/'

# Time (in seconds) for sleep between each call to avoid exceeding quota.
# Default to 3 as free subscription have limit of 20 calls per minute.
TIME_SLEEP = 3