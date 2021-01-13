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
       while read p ; do
        if [ "$i" -gt "3" ]; then
                echo ""$vmname".s."$i""
                i=$((i+1))
        else
                echo ""$vmname".m."$i""
                i=$((i+1))
        fi
        done < name.txt
        >name.txt
      
    '''
 }
 
return this 
