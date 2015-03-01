package asset.pipeline.react

import asset.pipeline.AbstractAssetFile

import java.util.regex.Pattern

class JsxAssetFile extends AbstractAssetFile {
    static final String contentType = ['application/javascript','application/x-javascript','text/javascript']
    static extensions = ['js', 'jsx']
    static compiledExtension = 'js'
    static processors = [ReactProcessor]
    Pattern directivePattern = ~/(?m)^\/\/=(.*)/
}