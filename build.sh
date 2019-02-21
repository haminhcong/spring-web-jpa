#!/usr/bin/env bash

sub_setup(){
    echo "Setup host for deploying app, require sudo privileges"

    echo "Configuring repo..."
    sudo yum update -y

    echo "Install packages..."
    sudo yum install -y docker-ce fish vim git telnet python-pip

    echo "Install Docker-compose via pip.."
    sudo pip install docker-compose

    echo "Configuring sysctl..."
    sudo cp cicd/docker/sysctl.conf /etc/
    sudo sysctl --load=/etc/sysctl.conf

    echo "Configuring system limits..."
    sudo cp cicd/docker/limits.conf /etc/security/
}

sub_stack-stop() {
    docker-compose -f cicd/docker-compose-base.yml -f cicd/docker-compose-dev-and-build.yml stop $1
}

sub_stack-build() {
    sub_package $1
    # build docker image from source code and tag with correct docker registry and version
    docker-compose -f cicd/docker-compose-base.yml -f cicd/docker-compose-dev-and-build.yml build $1
}

sub_stack-restart() {

  echo "Restarting $1..."
  docker-compose -f cicd/docker-compose-base.yml -f cicd/docker-compose-dev-and-build.yml stop $1
  sub_stack-build $1
  echo "Starting $1..."
  docker-compose -f cicd/docker-compose-base.yml -f cicd/docker-compose-dev-and-build.yml up -d $1
  echo "Wait for $1 port is up..."

  NEXT_WAIT_TIME=0
  CONTAINER_HEALTH_CHECK=$(docker inspect -f '{{json .State.Health.Status}}' $1 | tr -d \" )

  while [[ ${CONTAINER_HEALTH_CHECK} != "healthy" ]];  do
    echo "Health status is ${CONTAINER_HEALTH_CHECK}"
    echo "Waits in ${NEXT_WAIT_TIME} s..."
    CONTAINER_HEALTH_CHECK=$(docker inspect -f '{{json .State.Health.Status}}' $1 | tr -d \")
    ((NEXT_WAIT_TIME++))
    sleep 1;
  done;

  echo "Final Health status is ${CONTAINER_HEALTH_CHECK}"
  echo "Restart done"
}

sub_package() {
  module=''
  if [[ -z $1 ]]; then
    echo "Packaging for all modules"
  elif [[ -n $1 ]]; then
    echo "Packaging for module" $1
    module='-am -pl '$1
  fi
  _docker_mvn package ${module} -DskipTests
}

sub_stack-clean() {
  module='-am -pl '$1
  echo "Clean for module" $1
  _docker_mvn clean ${module} -DskipTests



}

_docker_mvn() {
    docker run -ti --rm \
        -v ~/.m2:/var/maven/.m2 \
        -e MAVEN_CONFIG=/var/maven/.m2 \
        -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven \
        -u $(id -u ${USER}) \
        maven:3.5.3-jdk-10 \
        mvn -Duser.home=/var/maven  clean $@
}


subcommand=$1

case $subcommand in
    "" | "-h" | "--help")
        sub_help
        ;;
    *)
        shift
        sub_${subcommand} $@
        if [ $? = 127 ]; then
            echo "Error: '$subcommand' is not a known subcommand." >&2
            echo "Run '$cmd --help' for a list of known subcommands." >&2
            exit 1
        fi
        ;;
esac
