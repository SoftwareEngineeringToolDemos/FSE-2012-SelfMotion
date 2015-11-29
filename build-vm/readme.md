Instructions


1. Download and install [Vagrant](https://www.vagrantup.com/downloads.html).

2. Download and install [VirtualBox](https://www.virtualbox.org/wiki/Downloads).

3. Open terminal or command prompt.

4. Type in 'vagrant init nritholtz/ubuntu-14.04.1'. This will create a Vagrantfile.

5. Download the Vagrantfile in this repo and replace the Vagrantfile created in the directory.

6. Type in 'vagrant up' to start the machine.

7. After vagrant is done running the script, open VirtualBox and power on the machine.

8. To use SelfMotion, read the readme.txt in the machine or in [this](https://github.com/SoftwareEngineeringToolDemos/FSE-2012-SelfMotion/blob/master/build-vm/Readme.txt) repository.

----------------------------------------------------------------------------------------------------------------------------

Note:
The machine should already log in, however if there is a case where you need the password, it is 'vagrant'.

----------------------------------------------------------------------------------------------------------------------------

Script sources:

http://thediscoblog.com/blog/2013/11/18/provisioning-ubuntu-with-java-in-3-steps/

https://docs.vagrantup.com/v2/getting-started/provisioning.html

https://github.com/aglover/ubuntu-equip/raw/master/equip_java8.sh
