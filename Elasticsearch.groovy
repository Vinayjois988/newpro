import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.json.JsonBuilder 

node {
  def vmname="Elasticsearh"
  def vmsize="20"
  def vmtype="t2.medium"
  def ostype="RHEL"
  def imageid="ami-0a9d27a9f4f5c0efc"
  def vmcount="8"
  def keyname="Jenkins"
  def securitygroupid=""
  def subnetid=""
  def rootdir="/var/lib/jenkins"
  def instanceid
  def ip
  
  
  stage (' Loading common files'){
  checkout scm
    common=load "${rootdir}/newpro/Common.groovy"
  }
   stage ('adding Elasticsearch name for created vms'){
    common.create_name(vmname)
   }
   stage (' ssh to existing vm'){
   common.ssh()
  }
  stage (' Installing Elasticsearch'){
    common.Elinstall(instanceid)
  }
  stage (' Installing Logstash'){
    common.loginstall(instanceid)
  }
  stage ('metricbeat installating'){
    common.metbeat()
  }
  stage ('Configure Kibana'){
    common.kibanainstall()
  }
}
