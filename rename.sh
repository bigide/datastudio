for ff in `cat rename.log`;do
	echo $ff
	echo ${ff/dinky/datastudio}
	mv $ff ${ff/dinky/datastudio}
done
