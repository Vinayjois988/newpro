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
       jenkinsid="i-05513a9dde52fac1b"
       okd="i-0905226405c3a9ee0"
       while read p ; do
        if [ "$i" -gt "3" ]; then
                if [ "$p" == "$jenkinsid" ]; then
                echo "jenkins id found"
                elif [ "$p" =="$okd" ]; then
                echo "okd found"
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
       jenkinsid="i-05513a9dde52fac1b"
       okd="i-0905226405c3a9ee0"
       user="ec2-user"
       while read p ; do
       if [ "$p" == "$jenkinsid" ]; then
       echo "jenkins id found"
       elif [ "$p" == "$okd" ]; then
       echo "okd found"
     
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
def cluster(){
 sh '''
      jenkinsid="i-05513a9dde52fac1b"
      okd="i-0905226405c3a9ee0"
      sudo aws ec2 describe-instances --output json | grep InstanceId | awk '{print $2}' | tr '"' ' ' | tr ',' ' ' > name.txt
      echo "sudo su
           cd /home/ec2-user/redis-6.0.9
           " >> /tmp/cluster.sh
      printf "echo "yes" | src/redis-cli --cluster create --cluster-replicas 1 " >> /tmp/cluster.sh
      while read p; do
      if [ "$p" == "$jenkinsid" ]; then
       echo "jenkins id found"
       elif [ "$p" == "$okd" ]; then 
       echo "okd found"
       else 
      Ip=$(sudo aws ec2 describe-instances --instance-ids="$p"  --query 'Reservations[*].Instances[*].{Instance:PublicIpAddress}')
      printf " $Ip:6379" >> /tmp/cluster.sh
      fi
      done < name.txt
      echo " -a Af1AMNF5Tl1" >> /tmp/cluster.sh
      
  
      sudo aws ec2 describe-instances --output json | grep InstanceId | awk '{print $2}' | tr '"' ' ' | tr ',' ' ' > name.txt
       user="ec2-user"
       while read p; do
       if [ "$p" == "$jenkinsid" ]; then
       echo "Jenkins id found"
       elif [ "$p" == "$okd" ]; then
       echo "okd found"
       else
       Ip=$(sudo aws ec2 describe-instances --instance-ids="$p"  --query 'Reservations[*].Instances[*].{Instance:PublicIpAddress}')
       sudo ssh -o "StrictHostKeyChecking no" -i "/tmp/Jenkins.pem" "$user"@$Ip 'bash -s' < /tmp/cluster.sh
       
       break
       fi
       done < name.txt
       >/tmp/cluster.sh
      '''
}
def get_cluster(){
 sh '''
      jenkinsid="i-05513a9dde52fac1b"
      okd="i-0905226405c3a9ee0"
   sudo aws ec2 describe-instances --output json | grep InstanceId | awk '{print $2}' | tr '"' ' ' | tr ',' ' ' > name.txt
   sudo aws ec2 describe-instances --output json | grep InstanceId | awk '{print $2}' | tr '"' ' ' | tr ',' ' ' > name.txt
       user="ec2-user"
       while read p; do
       if [ "$p" == "$jenkinsid" ]; then
       echo "Jenkins id found"
       elif [ "$p" == "$okd" ]; then
       echo "okd found"
       else
       echo "sudo su
           cd /home/ec2-user/redis-6.0.9
           " >> /tmp/cluster.sh
       echo 'src/redis-cli -c -h "`hostname -i`" -a Af1AMNF5Tl1 info' >> /tmp/cluster.sh
       echo 'src/redis-cli -c -h "`hostname -i`" -a Af1AMNF5Tl1 cluster nodes' >> /tmp/cluster.sh
       
       Ip=$(sudo aws ec2 describe-instances --instance-ids="$p"  --query 'Reservations[*].Instances[*].{Instance:PublicIpAddress}')
       sudo ssh -o "StrictHostKeyChecking no" -i "/tmp/Jenkins.pem" "$user"@$Ip 'bash -s' < /tmp/cluster.sh
       break
       fi
       done < name.txt
       >/tmp/cluster.sh
 '''
}
    
return this 
