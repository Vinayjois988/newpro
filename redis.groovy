import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.json.JsonBuilder 

node {
  def vmname="Vm1"
  def vmsize="8"
  def vmtype="t2.micro"
  def ostype="RHEL"
  def imageid="ami-077e31c4939f6a2f3"
  def vmcount="1"
  def keyname="Jenkins"
  def securitygroupid="sg-0c0ff2aed6b3cac3"
  def subnetid="sub"
  net-dc5068b4"
  def rootdir="/var/lib/jenkins"
  def instanceid
  def ip
  def jenkinsid="i-02f8d269fceb2d1bf"
  def okd="i-05f223c859b90532d"
  def user="ec2-user"
  def p
  
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
  stage ('Creating New Vm'){
   common.vm_creation(imageid,vmcount,vmtype,keyname,securitygroupid,subnetid) 
  }
  stage ('adding name for created vms'){
    common.create_name(vmname)
  }
  /* stage (' Waiting till all vms come'){
    sh """ sleep 60 """
  }
  stage ( ' Call ssh '){
    common.ssh()
  }
    stage ('Redis Cluster'){
      common.cluster()
  }
  stage ('getig info of cluster'){
    common.get_cluster(jenkinsid,okd,p)
  } */
}
 return this ;
