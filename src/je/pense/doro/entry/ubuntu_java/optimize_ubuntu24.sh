#!/bin/bash
sudo apt update && sudo apt upgrade -y
sudo apt autoremove -y
sudo sync && sudo sysctl -w vm.drop_caches=3
sudo swapoff -a && sudo swapon -a
sudo rm -rf /tmp/* /var/tmp/*
sudo fstrim -av
sudo apt install preload -y
sudo systemctl enable --now preload
sudo apt install mesa-utils intel-gpu-tools mesa-va-drivers intel-media-va-driver-non-free vainfo -y

