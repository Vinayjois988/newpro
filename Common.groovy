import java.util.regex.Matcher;		
import java.util.regex.Pattern;		
import groovy.json.JsonSlurper;
import groovy.json.JsonSlurperClassic
import groovy.json.*

import java.util.Map;
import java.io.Reader;
import java.util.HashMap;


def mobile (def vmname){
 sh """
 	echo "9972"
  echo "${vmname}"
    """
}

def vm_creation (def imageid,def vmcount,def vmtype,def keyname,def securitygroupid,def subnetid){
 sh """
    sudo aws ec2 run-instances --image-id ${imageid} --block-device-mappings file:///tmp/maping.json --count ${vmcount} --instance-type ${vmtype} --key-name ${keyname} --security-group-ids ${securitygroupid} --subnet-id ${subnetid}
"""
}
def create_name (def vmname){
  sh '''
       sudo aws ec2 describe-instances --output json | grep InstanceId | awk '{print $2}' | tr '"' ' ' | tr ',' ' ' > name.txt
       vmname="redis"
       i=1
       jenkinsid="i-0a30cc8e6622f9ef3"
       while read p ; do
        if [ "$i" -gt "3" ]; then
                if [ "$p" == "$jenkinsid" ]; then
                echo "jenkins id found"
                else 
                sudo aws ec2 create-tags --resources "$p" --tags Key=Name,Value="$vmname".s.vm"$i"
                i=$((i+1))
                fi
        else
                if [ "$p" == "$jenkinsid" ]; then
                echo "jenkins id found"
                else
                sudo aws ec2 create-tags --resources "$p" --tags Key=Name,Value="$vmname".m.vm"$i"
                i=$((i+1))
                fi
                
        fi
        done < name.txt
        >name.txt
      
    '''
}
 def ssh (){
  sh '''
       sudo aws ec2 describe-instances --output json | grep InstanceId | awk '{print $2}' | tr '"' ' ' | tr ',' ' ' > name.txt
       jenkinsid="i-0a30cc8e6622f9ef3"
       user="ec2-user"
       while read p ; do
       if [ "$p" == "$jenkinsid" ]; then
       echo "jenkins id found"
       
       
       else
       Ip=$(sudo aws ec2 describe-instances --instance-ids="$p"  --query 'Reservations[*].Instances[*].{Instance:PublicIpAddress}')
       #sudo ssh -o "StrictHostKeyChecking no" -i "/tmp/Jenkins.pem" "$user"@$Ip 
       sudo scp -o 'StrictHostKeyChecking no' -i "/tmp/Jenkins.pem" /tmp/remotescript.sh "$user"@$Ip:/tmp
       sudo ssh -o "StrictHostKeyChecking no" -i "/tmp/Jenkins.pem" "$user"@$Ip 'bash -s' < /tmp/remotescript.sh
       fi
       done < name.txt
       >name.txt
     '''
   }
 def cluster()
 sh '''
      sudo aws ec2 describe-instances --output json | grep InstanceId | awk '{print $2}' | tr '"' ' ' | tr ',' ' ' > name.txt
      echo "sudo su
           cd /home/ec2-user/redis-6.0.9
           " >> /tmp/cluster.sh
      printf "src/redis-cli --cluster create --cluster-replicas 1 " >> /tmp/cluster.sh
      while read p; do

      Ip=$(sudo aws ec2 describe-instances --instance-ids="$p"  --query 'Reservations[*].Instances[*].{Instance:PublicIpAddress}')
      printf " $Ip:6379" >> /tmp/cluster.sh
      done < name.txt
      echo " -a Atos@123" >> /tmp/cluster.sh
      
      sudo aws ec2 describe-instances --output json | grep InstanceId | awk '{print $2}' | tr '"' ' ' | tr ',' ' ' > name.txt
      while read p; do
      Ip=$(sudo aws ec2 describe-instances --instance-ids="$p"  --query 'Reservations[*].Instances[*].{Instance:PublicIpAddress}')
      sudo ssh -o "StrictHostKeyChecking no" -i "/tmp/Jenkins.pem" "$user"@$Ip 'bash -s' < /tmp/cluster.sh
      break 
      done < name.txt
      '''
}
    
return this 
