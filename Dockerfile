FROM ubuntu:14.04

##########
## R
##########
RUN echo 'install.packages(\n\
	c(\n\
		"ggplot2",\n\
		"reshapei2",\n\
		"jsonlite",\n\
		"plyr"\n\
	),\n\
	dependencies=T,\n\
	repos="http://cran.ma.imperial.ac.uk"\n\
)'\
>> install.r
RUN apt-get update && \
apt-get install -y r-base && \
Rscript install.r

##########
## Java
##########
RUN apt-get update &&\
	apt-get install -y software-properties-common && \
	add-apt-repository -y ppa:openjdk-r/ppa && \
	apt-get update && \
	apt-get install -y openjdk-8-jre

##########
## Crosser
##########
RUN curl -L https://dl.bintray.com/tearne/generic/crosser-0.2.10.tar.gz \
| tar xz --strip 1 

