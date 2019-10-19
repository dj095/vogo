#!/usr/bin/env bash
OS="$(uname -s)"
if [ $OS = "Darwin" ]
then
    ln -sf $1 ./build/libs/kalaari.jar
elif [ $OS = "Linux" ]
then
    ln -rsf $1 ./build/libs/kalaari.jar
fi