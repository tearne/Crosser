require(ggplot2)

data = read.csv('Out.csv')
head(data)

ggplot(data, aes(x=PropD1)) + geom_density() + scale_x_continuous(limits=c(0,1))