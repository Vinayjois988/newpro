import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.json.JsonBuilder 

node {
  def vmname="Redis"
  def vmsize="20"
  def vmtype="t2.medium"
  def ostype="RHEL"
  def imageid="ami-096fda3c22c1c990a"
  def vmcount="6"
  def keyname="Jenkins"
  def securitygroupid="sg-04671d091eb987414"
  def subnetid="subnet-0bab0452812a186de"
  def rootdir="/var/lib/jenkins"
  def instanceid
  def ip
  
  stage ('Loading common Files'){
    checkout scm
    common=load "${rootdir}/newpro/Common.groovy"
  }
  stage ('echoing all varribles'){
    sh """echo hi """
  }
  stage ('Echo my mobile number'){
    common.mobile(vmname)
  }
  stage ('Creating VMs for redis'){
   common.vm_creation(imageid,vmcount,vmtype,keyname,securitygroupid,subnetid) 
  }
  stage ('adding name for created vms'){
    common.create_name(vmname)
  }
  stage (' Waiting till all vms come'){
    sh """ sleep 60 """
  }
  stage ( ' Call ssh '){
    common.ssh()
    stage ('Redis Cluster'){
      common.cluster()
  }
}
  
