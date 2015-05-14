package asset.pipeline.react

import asset.pipeline.AbstractAssetFile

import java.util.regex.Pattern

class CjsxAssetFile extends AbstractAssetFile {
    static
    final List<String> contentType = ['text/jsx', 'application/javascript', 'application/x-javascript', 'text/javascript']
    static List<String> extensions = ['cjsx']
    static String compiledExtension = 'js'
    static processors = [CoffeeReactProcessor]
    Pattern directivePattern = ~/(?m)^\/\/=(.*)/
}