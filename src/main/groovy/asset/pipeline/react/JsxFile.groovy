package asset.pipeline.react

import asset.pipeline.AbstractAssetFile

class JsxFile extends AbstractAssetFile {
    static final String contentType = 'text/javascript'
    static extensions = ['jsx']
    static compiledExtension = 'js'
    static processors = [ReactProcessor]

    String directiveForLine(String line) {
        line.find(/\/\/=(.*)/) { fullMatch, directive -> return directive }
    }
}