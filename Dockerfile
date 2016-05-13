FROM ubuntu:16.04

RUN echo "deb http://cran.ma.imperial.ac.uk/bin/linux/ubuntu xenial/" >> /etc/apt/sources.list && \
    apt-key adv --keyserver keyserver.ubuntu.com --recv-keys E084DAB9

RUN apt-get update && \
    apt-get install -y software-properties-common && \
    add-apt-repository -y ppa:openjdk-r/ppa && \
    apt-get update && \
    apt-get install -y --no-install-recommends openjdk-8-jre curl r-base build-essential && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN echo 'install.packages(\n\
	c(\n\
		"ggplot2",\n\
		"reshape",\n\
		"jsonlite",\n\
		"plyr"\n\
	),\n\
	dependencies=T,\n\
	repos="http://cran.ma.imperial.ac.uk"\n\
)'\
>> install.r

RUN Rscript install.r

#############
## Crosser
#############
RUN curl -L https://dl.bintray.com/tearne/generic/crosser-0.2.10-3.tgz \
| tar xz --strip 1 
