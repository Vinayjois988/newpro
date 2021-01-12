import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.json.JsonBuilder 

node {
  def vmname="Redis"
  def vmsize="20"
  def vmtype="t2.medium"
  def ostype="RHEL"
  def imageid="ami-0a9d27a9f4f5c0efc"
  def vmcount="6"
  def keyname="Jenkins"
  def securitygroupid="sg-8ed10bec"
  def subnetid="subnet-dc5068b4"
  def rootdir="/var/lib/jenkins"
  def instanceid
  
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
  //stage ('Creating VMs for redis'){
   //common.vm_creation(imageid,vmcount,vmtype,keyname,securitygroupid,subnetid) 
  //}
  stage ('adding name for created vms'){
    common.create_name(instanceid,vmname)
  }
  }

  
