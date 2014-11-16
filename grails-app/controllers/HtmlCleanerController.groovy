
class HtmlCleanerController {

    def index() {

    }

    def clean(String unsafe) {
        String result = cleanHtml(unsafe, "basic")
        return [result:result]
    }
}
