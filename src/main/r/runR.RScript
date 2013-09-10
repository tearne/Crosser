#! /usr/bin/Rscript

workingDir <- commandArgs(TRUE)[1]

require(grid)
require(ggplot2)
require(reshape)

draw <- function(fileName){
	colours = 1:9
	
	getData <- function(tidId, plantId){
		dataColIds = (3:ncol(raw))
		tidData = raw[raw$tidId == tidId,dataColIds]
		width = tidData[,plantId]
		if(plantId > 2){	
			leftPoint = rowSums(tidData[,1:max(1,(plantId-1))])
		} else if(plantId == 2){
			leftPoint = tidData[,1] 
		} else {
			leftPoint = rep(0,nrow(tidData))
		}
		data.frame(leftPoint, width, cMId=1:nrow(tidData))
	}

	drawContribution <- function(tidId, plantId){
		data = getData(tidId, plantId)
		if(tidId %% 2 == 0){
			xPositions = data$leftPoint
			justify = c("left", "centre")
		} else {
			xPositions = 1 - data$leftPoint
			justify = c("right", "centre")
		}
		pushViewport(viewport(width = unit(0.9, "npc"), height = unit(1, "npc"), x = unit(0.5, "npc"), y = unit(0.5, "npc"), just = "centre", yscale = c(0,250)))
		grid.rect(x = xPositions, y = unit(data$cMId, "native"), height = unit(1,"native"), width = data$width, just = justify, gp = gpar(fill = colours[plantId], lty="blank", lwd = 0))
		popViewport()
	}
	
	drawTid <- function(tidId){
		pushViewport(viewport(layout.pos.col = (tidId + 1), layout.pos.row = 1, yscale = c(0,250)))
		mapply(drawContribution, tidId, 1:numPlants)
		popViewport()
	}
	
	legend <- function(plantNames){
		boxSize = unit(0.2, "inches")
		nLabels = length(plantNames)
		pushViewport(viewport(layout = grid.layout(nLabels,1)))
		for(i in 1:nLabels){
			pushViewport(viewport(layout.pos.row = i))
			grid.rect(width = boxSize, height = boxSize, just = "bottom", gp = gpar(fill = colours[i]))
			grid.text(plantNames[i], y = unit(0.5,"npc") - unit(1, "lines"), gp=gpar(fontsize=7))
			popViewport()
		}
		popViewport()
	}

	raw = read.csv(fileName)
	maxTidIndex = max(raw$tidId)
	numPlants = ncol(raw) - 2

	grid.newpage()
	
	pushViewport(viewport(x = 1, y = 1, width = unit(0.5, "npc"), height = unit(0.05, "npc"), just = c("right","top")))
	grid.text(fileName)
	popViewport()
	
	pushViewport(viewport(layout = grid.layout(1,maxTidIndex+3)))
	mapply(drawTid, 0 : maxTidIndex)
	
	pushViewport(viewport(layout.pos.col = (maxTidIndex+2):(maxTidIndex+3), layout.pos.row = 1))
	plantNames = colnames(raw[,(3:ncol(raw))])
	legend(plantNames)
	popViewport()
}

pdf(paste(workingDir, "plots.pdf", sep="/"), width=8.27, height=5.83) #A7 landscape paper

fileNames <- list.files(path = workingDir, pattern = "density.csv")
fileNames <- lapply(fileNames, function(x) paste(workingDir, x, sep = "/"))
data = melt(lapply(fileNames, read.csv))
ggplot(data, aes(x=value)) + 
	geom_density(aes(colour=variable)) + 
	scale_x_continuous(limits=c(0,1)) +
	labs(title="Distribution of Contribution")

fileName = "ProbSuccess.csv"
fileName = paste(workingDir, fileName, sep = "/")
data = melt(read.csv(fileName))
levelsOrdering = read.csv(fileName, as.is=T)$CrossName
data$CrossName = factor(data$CrossName, levels = levelsOrdering)
ggplot(data, aes(x=CrossName, y=value)) + 
	geom_bar(stat = "identity") +
	labs(title="Probability of Selection Success")

fileName = "MeanCrossComposition.csv"
fileName = paste(workingDir, fileName, sep = "/")
data = read.csv(fileName)
raw = read.csv(fileName, as.is=T)
data$Cross = factor(data$Cross, levels = unique(raw$Cross))
data$Donor = factor(data$Donor, levels = unique(raw$Donor))
ggplot(data, aes(x=Cross, colour=Donor, fill=Donor, y=MeanProportion)) +
	geom_bar(stat="identity", linetype='blank') +
	scale_y_continuous(name="Mean Proportions") +
	labs(title="Mean Cross Composition")

fileNames = list.files(path = workingDir, pattern = "composition.csv")
fileNames <- lapply(fileNames, function(x) paste(workingDir, x, sep = "/"))
invisible(lapply(fileNames, draw))

dev.off()