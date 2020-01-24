# Simple Spark
A simple framework for executing general Spark batch jobs

## Usage
Implement a Spark job by creating an object that extends the SparkJob trait.

Override the `execute` method

There are a few default parameters, but you can add your own via keyword args. These are described in the `CommandLineOptions` class.

Run it.

## Running locally
Via IntelliJ, set the following parameters in your run config for your job and it should run nicely on your laptop.

```
-Dspark.master=local[2]
-Dspark.master.host=127.0.0.1
-Dspark.driver.bindAddress=127.0.0.1
-Dspark.driver.host=localhost
```

## Running directly on EMR

```bash
aws emr ssh --cluster-id <cluster> --key-pair-file ~/.ssh/<your-key>.pem
spark-submit --master yarn --deploy-mode cluster --class <class> <s3_path_to_jar> <parameters>

```
