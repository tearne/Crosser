
# Spikes.tristanbinomial.Prog gives
# n = 100
# r = 20
# p = 0.2
# ans = 0.4601613700645752
#
#double totalprob(int n, int r, double p) {//the prob of at most r successes out of n, which decreases with n at least r//
#	double q=1-p,temp,result=0;
#	int j=0;
#	temp=pow(q,(double)n);
#	for(j=0;j<r;j++) {
#		result=result+temp;
#		temp=(temp*p*(double)(n-j))/(q*(double)(j+1));
#	}
#	return result;
#}

# Cumulative distribution
pbinom(20, 100, 0.2)

# How many plants are needed to have a 95% change of 20 successes when p=0.1
pbinom(19, 275, 0.1, lower.tail=F) #286
pbinom(19,275,0.1)
