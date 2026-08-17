const fs = require("fs");
const js = fs.readFileSync("live_bundle.js", "utf8");

function extractBalancedArray(code, startIndex) {
  let depth = 0;
  let inString = false;
  let quoteChar = '';
  let isEscaped = false;
  let end = startIndex;

  for (let i = startIndex; i < code.length; i++) {
    const char = code[i];
    if (isEscaped) {
      isEscaped = false;
      continue;
    }
    if (char === '\\') {
      isEscaped = true;
      continue;
    }
    if (inString) {
      if (char === quoteChar) {
        inString = false;
      }
      continue;
    } else {
      if (char === '`' || char === '"' || char === "'") {
        inString = true;
        quoteChar = char;
        continue;
      }
      if (char === '[') {
        depth++;
      } else if (char === ']') {
        depth--;
        if (depth === 0) {
          end = i + 1;
          break;
        }
      }
    }
  }
  return code.substring(startIndex, end);
}

const artStart = js.indexOf("[{number:");
const artRaw = extractBalancedArray(js, artStart);
console.log("Extracted articles raw length:", artRaw.length);

// Test parsing with js-to-json converter
function jsToJson(jsCode) {
  // Replace backtick string literals with standard JSON strings
  // Handle newlines, quotes inside backticks
  let out = "";
  let i = 0;
  while (i < jsCode.length) {
    if (jsCode[i] === '`') {
      let str = "";
      i++;
      while (i < jsCode.length) {
        if (jsCode[i] === '\\' && i + 1 < jsCode.length) {
          str += jsCode[i] + jsCode[i+1];
          i += 2;
          continue;
        }
        if (jsCode[i] === '`') {
          i++;
          break;
        }
        str += jsCode[i];
        i++;
      }
      out += JSON.stringify(str);
    } else {
      out += jsCode[i];
      i++;
    }
  }
  
  // Replace unquoted property keys: {slug: or ,count:
  out = out.replace(/([{,]\s*)([a-zA-Z0-9_]+)\s*:/g, '$1"$2":');
  // Remove trailing commas before } or ]
  out = out.replace(/,\s*([}\]])/g, '$1');
  return out;
}

try {
  const jsonStr = jsToJson(artRaw);
  const parsed = JSON.parse(jsonStr);
  console.log("Successfully parsed articles count with jsToJson:", parsed.length);
} catch (e) {
  console.error("jsToJson error:", e.message);
}
