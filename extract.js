const fs = require("fs");
const js = fs.readFileSync("/tmp/notes_repo/assets/index-CMTjVc30.js", "utf8");

console.log("Looking for categories, articles, words in bundle...");

const keywords = ["AI 工程深度", "Vibe Coding", "文章总览", "阅读时间线", "概念词典"];
for (const kw of keywords) {
  const idx = js.indexOf(kw);
  console.log(`Keyword "${kw}" at:`, idx);
  if (idx !== -1) {
    console.log("Context around:\n", js.substring(Math.max(0, idx - 200), Math.min(js.length, idx + 400)));
    console.log("-----------------------------------------");
  }
}
