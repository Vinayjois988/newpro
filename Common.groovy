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
       while read p; do
       echo "$p"
       i=1
       sudo aws ec2 create-tags --resources "$p" --tags Key=Name,Value="$vmname".m.vm"$i"
       i=$((i+1))
       if $i>"3"; then 
       i=4
       sudo aws ec2 create-tags --resources "$p" --tags Key=Name,Value="$vmname".s.vm"$i"
       else 
       echo "nothing"
       fi
       done <name.txt
      
      
      >name.txt
      #myvar=sachin
       #echo \$myvar
    '''
 }
 
return this 
