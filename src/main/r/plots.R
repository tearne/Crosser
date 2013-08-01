require(ggplot2)
require(reshape)

files <- list.files(pattern = ".density.csv")
data = melt(lapply(files, read.csv))
ggplot(data, aes(x=value)) + geom_density(aes(colour=variable)) + scale_x_continuous(limits=c(0,1))

for (i in seq_along(files)) {

}
    