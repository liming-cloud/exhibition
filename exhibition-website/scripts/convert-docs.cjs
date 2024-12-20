// scripts/convert-docs.cjs
const fs = require('fs')
const path = require('path')
const mammoth = require('mammoth')
const exhibitions = require('../content/exhibitions.json')

const inputRoot = path.join(__dirname, '..', 'public', 'exhibitions')
const outputRoot = path.join(__dirname, '..', 'public', 'docs')

if (!fs.existsSync(outputRoot)) {
    fs.mkdirSync(outputRoot, { recursive: true })
}

for (const e of exhibitions) {
    if (!e.file) continue
    const fileExtension = e.file.split('.').pop().toLowerCase()
    if (fileExtension === 'docx') {
        const docxPath = path.join(inputRoot, e.file.replace(/^\/exhibitions\//, ''))
        if (fs.existsSync(docxPath)) {
            console.log('Converting docx:', docxPath)
            const arrayBuffer = fs.readFileSync(docxPath)
            mammoth.convertToHtml({ buffer: arrayBuffer })
                .then(result => {
                    const html = result.value
                    const relative = e.file.replace(/^\/exhibitions\//, '')
                    const baseName = relative.replace(/\.[^.]+$/, '')
                    const outputFilePath = path.join(outputRoot, baseName + '.html')
                    const outputFileDir = path.dirname(outputFilePath)
                    if (!fs.existsSync(outputFileDir)) {
                        fs.mkdirSync(outputFileDir, { recursive: true })
                    }
                    fs.writeFileSync(outputFilePath, html, 'utf-8')
                    console.log(`Saved: ${outputFilePath}`)
                })
                .catch(err => console.error('Error converting docx:', docxPath, err))
        } else {
            console.warn('Docx file not found:', docxPath)
        }
    }
}