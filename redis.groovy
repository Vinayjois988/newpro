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
  def ip
  def jenkinsid="i-05513a9dde52fac1b"
  def okd="i-0535ccefeed795041"
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
  }
    stage ('Redis Cluster'){
      common.cluster()
  }
  stage ('getig info of cluster'){
    common.get_cluster(jenkinsid,okd,p)
  }
}
 return this ;
