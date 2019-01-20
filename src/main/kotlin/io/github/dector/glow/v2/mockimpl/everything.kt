package io.github.dector.glow.v2.mockimpl

import io.github.dector.glow.v2.core.*
import java.io.File


class MockDataProvider : DataProvider {

    override fun fetchMetaInfo() = MetaInfo(
            pages = listOf(
                    PageInfo(1, "Index", File("v2/src/pages/index.md")),
                    PageInfo(2, "Projects", File("v2/src/pages/projects.md")),
                    PageInfo(3, "About", File("v2/src/pages/about.md"))
            )
    )

    override fun fetchBlogData(): BlogData {
        error("Shouldn't be called")
    }
}

class MockDataProcessor : DataProcessor {

    override fun processBlogData(blog: BlogData): ProcessedData {
        TODO()
    }
}

class MockDataPublisher : DataPublisher {

    override fun publish(data: ProcessedData): PublishResult {
        TODO()
    }
}