def call() {

    // BẮT BUỘC: phải checkout trước
    def branch = env.GIT_LOCAL_BRANCH ?: env.GIT_BRANCH

    if (!branch) {
        error "❌ Cannot detect git branch. Did you checkout repository?"
    }

    // Chuẩn hoá branch
    branch = branch.replaceFirst(/^origin\//, '')

    echo "🔍 Current branch: ${branch}"

    // ❌ Cấm deploy main
    if (branch == 'main') {
        error "❌ Deploy from 'main' branch is NOT allowed"
    }

    // ✅ Chỉ cho phép dev* và hot-fix*
    if (!(branch ==~ /dev.*/ || branch ==~ /hot-fix.*/)) {
        error """
❌ Branch '${branch}' is NOT allowed
Allowed branches:
 - dev*
 - hot-fix*
"""
    }

    echo "✅ Branch '${branch}' passed validation"
}
