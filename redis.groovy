import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.json.JsonBuilder 

node {
  def vmname="Vm1"
  def vmsize="8"
  def vmtype="t2.micro"
  def ostype="RHEL"
  def imageid="ami-010aff33ed5991201"
  def vmcount="1"
  def keyname="newvm"
  def securitygroupid="sg-022be2db2733d8c9d"
  def subnetid="subnet-06856d6d"
  def rootdir="/var/lib/jenkins"
  def instanceid
  def ip
  //def jenkinsid="i-03db02cf586d93cd6"
  //def okd=""
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
