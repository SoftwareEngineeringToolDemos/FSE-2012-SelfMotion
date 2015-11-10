Instructions


1. Open up terminal or command prompt.

2. Type in 'vagrant init hashicorp/precise32'. This will create a Vagrantfile.

3. Open up the Vagrantfile and type in 'config.vm.provision :shell, path: "bootstrap.sh"' after 'config.vm.box_url = "http://files.vagrantup.com/precise64.box"'.

4. Type in 'vagrant up' to start the machine.

5. Type in 'vagrant ssh' to ssh into the machine.

6. If you would like to check the java version type in 'java -version'.
  
Script sources:

http://thediscoblog.com/blog/2013/11/18/provisioning-ubuntu-with-java-in-3-steps/

https://docs.vagrantup.com/v2/getting-started/provisioning.html

https://github.com/aglover/ubuntu-equip/raw/master/equip_java8.sh
