package io.github.dector.glow.v2.core


fun execute(dataProvider: DataProvider,
            dataProcessor: DataProcessor,
            dataPublisher: DataPublisher): Result {
    val data = dataProvider()
    val processedData = dataProcessor(data)
    val publishResult = dataPublisher(processedData)

    return Result(publishResult)
}