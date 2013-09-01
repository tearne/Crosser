require(ggplot2)
require(reshape)

pdf("plots.pdf", width=4.13, height=2.91) #A7 landscape paper

files <- list.files(pattern = "density.csv")
data = melt(lapply(files, read.csv))
ggplot(data, aes(x=value)) + geom_density(aes(colour=variable)) + scale_x_continuous(limits=c(0,1))

fileName = "ProbSuccess.csv"
data = melt(read.csv(fileName))
levelsOrdering = read.csv(fileName, as.is=T)$CrossName
data$CrossName = factor(data$CrossName, levels = levelsOrdering)
ggplot(data, aes(x=CrossName, y=value)) + geom_bar(stat = "identity")

fileName = "MeanCrossComposition.csv"
data = read.csv(fileName)
raw = read.csv(fileName, as.is=T)
data$Cross = factor(data$Cross, levels = unique(raw$Cross))
data$Donor = factor(data$Donor, levels = unique(raw$Donor))
ggplot(data, aes(x=Cross, colour=Donor, fill=Donor, y=MeanProportion)) +
	geom_bar(stat="identity", linetype='blank') +
	scale_y_continuous(name="Mean Proportions")

source("gridTest.r")
fileName = "output/BC2F1S4.composition.csv"
draw(fileName)

dev.off()    