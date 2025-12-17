#!/usr/bin/env groovy
def call(String pmEmail) {
    // Chỉ áp dụng cho main branch
    if (env.BRANCH_NAME != 'main') {
        echo "ℹ️ Not on main branch, skip PM notification"
        return
    }

    // Validate branch nguồn
    def source = env.CHANGE_BRANCH
    if (!(source == 'dev' || source.startsWith('hot-fix'))) {
        error """
❌ Merge blocked: Only 'dev' or 'hot-fix*' branches can merge into main.
Your branch: ${source}
"""
    }

    // Template mail HTML với link PR
    def mailBody = """
<html>
<head>
<style>
body { font-family: Arial, sans-serif; }
h2 { color: #2E86C1; }
p { font-size: 14px; }
a.button {
    display: inline-block;
    padding: 10px 20px;
    background-color: #2E86C1;
    color: white;
    text-decoration: none;
    border-radius: 5px;
}
</style>
</head>
<body>
<h2>PR cần PM duyệt</h2>
<p>PR từ nhánh: <b>${source}</b> muốn merge vào <b>main</b>.</p>
<p>Commit: ${env.GIT_COMMIT}</p>
<p>Link PR: <a class="button" href="${env.CHANGE_URL}">Click để xem & approve PR</a></p>
<p>Link repo: <a href="${env.GIT_URL}">${env.GIT_URL}</a></p>
</body>
</html>
"""

    // Gửi mail
    emailext(
        subject: "PR từ ${source} cần PM duyệt",
        body: mailBody,
        mimeType: 'text/html',
        to: pmEmail
    )

    echo "📧 Notification sent to PM at ${pmEmail}"
}
