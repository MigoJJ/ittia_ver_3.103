#!/bin/bash
# Ubuntu System Cleanup and Performance Optimization Script

echo ">>> Updating package lists..."
sudo apt update

echo ">>> Upgrading installed packages..."
sudo apt upgrade -y

echo ">>> Removing unnecessary packages..."
sudo apt autoremove -y

echo ">>> Cleaning up APT cache..."
sudo apt autoclean
sudo apt clean

echo ">>> Cleaning up journal logs older than 7 days..."
sudo journalctl --vacuum-time=7d

echo ">>> Removing old Snap package revisions..."
snap list --all | awk '/disabled/{print $1, $2}' | while read snapname version; do
  sudo snap remove "$snapname" --revision="$version"
done

echo ">>> Removing unused old kernels..."
sudo apt --purge autoremove -y

echo ">>> Enabling SSD trim if using SSD..."
sudo systemctl enable fstrim.timer
sudo systemctl start fstrim.timer

echo ">>> Cleanup complete. It's recommended to reboot the system."
