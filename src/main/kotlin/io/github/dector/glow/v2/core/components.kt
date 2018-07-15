package io.github.dector.glow.v2.core

typealias DataProvider = () -> BlogData
typealias DataProcessor = (BlogData) -> ProcessedData
typealias DataPublisher = (ProcessedData) -> PublishResult