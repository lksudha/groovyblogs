import net.sf.ehcache.Ehcache
import net.sf.ehcache.Element

class EntriesService {

    boolean transactional = false

    def entriesCache

    // suppress when more than three entries from same author
    public static def limitEntries(entries) {

        def authorHash = [ : ] // count by blog
        def limitEntries = [ ] // limit to three entries

        entries.each { entry ->
            def key = entry.blog.feedUrl
            int entryCount = authorHash[key] ? authorHash[key] : 0
            entryCount++
            authorHash[key] = entryCount
            if (entryCount <= 3) {
                if (entryCount == 3)
                entry.info = "Reached limit of 3 displayed entries shown for ${entry.blog.title}. " +
   						"<a href='../blog/show/${entry.blog.id}'>Read more...</a>"
                limitEntries << entry
            }
        }
        return limitEntries

    }


    def getEndlessEntries(params) {

        def entries = entriesCache.get("endlessList-${params.offset}")?.value
        if (!entries) {
            params.order = "desc"
            entries = BlogEntry.listOrderByDateAdded(params)
            entries = entries.findAll { entry -> entry.isGroovyRelated() }
            entriesCache.put(new Element("endlessList=${params.offset}", entries))
        }
        return entries

    }


    def getRecentEntries() {

        def entries = entriesCache.get("recentList")?.value
        if (!entries) {

            def aWhileAgo = new Date().minus(7) // 7 days ago

            entries = BlogEntry.findAllByDateAddedGreaterThan(
                aWhileAgo, [ sort: 'dateAdded', order: "desc" ] )
            entries = entries.findAll { entry -> entry.isGroovyRelated() }
            entriesCache.put(new Element("recentList", entries))
        }
        return entries

    }

    def getPopularEntries() {

        def entries = entriesCache.get("popularList")?.value
        if (!entries) {

            def aWhileAgo = new Date().minus(7) // 7 days ago

            entries = BlogEntry.findAllByDateAddedGreaterThanAndHitCountGreaterThan(
                aWhileAgo, 0, [ sort: 'hitCount', order: "desc" ] )
            entries = entries.findAll { entry -> entry.isGroovyRelated() }

            entriesCache.put(new Element("popularList", entries))
        }
        return entries

    }
}
