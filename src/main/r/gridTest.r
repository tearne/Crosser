library(grid)

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
	if(tidId %% 2 == 1){
		xPositions = data$leftPoint
		justify = c("left", "centre")
	} else {
		xPositions = 1 - data$leftPoint
		justify = c("right", "centre")
	}
	pushViewport(viewport(width = unit(0.9, "npc"), height = unit(1, "npc"), x = unit(0.5, "npc"), y = unit(0.5, "npc"), just = "centre", yscale = c(0,250)))
	grid.rect(x = xPositions, y = unit(data$cMId, "native"), height = unit(1,"native"), width = data$width, just = justify, gp = gpar(fill = plantId, lwd = 0))
	popViewport()
}

drawTid <- function(tidId, numPlants){
	pushViewport(viewport(layout.pos.col = (tidId + 1), layout.pos.row = 1, yscale = c(0,250)))
	mapply(drawContribution, tidId, 1:numPlants)
	popViewport()
}

draw <- function(fileName){
	raw = read.csv(fileName)
	maxTidIndex = max(raw$tidId)
	numPlants = ncol(raw) - 2

	pushViewport(viewport(layout = grid.layout(1,maxTidIndex+1)))
	mapply(drawTid, 0 : maxTidIndex, numPlants)
	popViewport()
}