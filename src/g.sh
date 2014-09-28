#/bin/bash
git add .
git commit -am $1 
git push -u origin master
sshpass -p 3Erozepln44 ssh amuhamm3@medusa-node1.vsnet.gmu.edu
