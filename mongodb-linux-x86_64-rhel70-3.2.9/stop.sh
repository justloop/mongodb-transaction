#!/bin/bash
ssh xcnd9 "pkill -f mongo"
ssh xcnd10 "pkill -f mongo"
ssh xcnd11 "pkill -f mongo"
echo "MongoDB stop successfully"
