SPARK_HOME=$1
MASTER=$2
RUN_CLASS=com.vinecom.rec.app.Main
APP_NAME="ItemBaseRecommend"
MODE=client
EXECUTOR_MEMORY=6g

COUNT=0
for f in lib/*.jar; do
        if [ $COUNT == 0 ]; then
                JARS="$f"
        else
            	JARS="$f,$JARS"
        fi
	COUNT=$(($COUNT+1))
done
$SPARK_HOME/bin/spark-submit --class $RUN_CLASS \
--jars $JARS \
--master $MASTER \
--name $APP_NAME \
--deploy-mode $MODE \
--conf "spark.executor.memory="$EXECUTOR_MEMORY \
vin-recommend-*.jar