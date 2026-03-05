set -euo pipefail

: "${BRANCH:?BRANCH is not set}"
: "${BOUNDARY:?BOUNDARY is not set}"
: "${PRIVATE_REMOTE:?}"
: "${PUBLIC_REMOTE:?}"
: "${PUBLIC_BASE_REF:?}"

git rebase --abort 2>/dev/null || true

git switch "$BRANCH"
git fetch "$PRIVATE_REMOTE" "$BRANCH"
git reset --hard "$PRIVATE_REMOTE/$BRANCH"

git merge-base --is-ancestor "$BOUNDARY" "$BRANCH" \
  || { echo "$BOUNDARY not in the hierarchy $BRANCH"; exit 1; }

git fetch "$PUBLIC_REMOTE" main
BASE_PARENT=$(git rev-parse "$PUBLIC_BASE_REF")

BOUNDARY_PARENT=$(git rev-parse "${BOUNDARY}^")
TREE_PARENT=$(git rev-parse "${BOUNDARY_PARENT}^{tree}")

PUBLIC_BASE=$(printf "Public history up to %s\n(internal %s)\n" \
                    "$BOUNDARY_PARENT" "$BOUNDARY_PARENT" \
              | git commit-tree -p "$BASE_PARENT" "$TREE_PARENT")

COMMITS=$(git rev-list --reverse --ancestry-path "${BOUNDARY}^..$BRANCH" \
          | tail -n +2)

NEW=$PUBLIC_BASE
for SRC in $COMMITS; do
    TREE=$(git rev-parse "${SRC}^{tree}")
    NEW=$(
        {
            git show -s --format=%B "$SRC"
            echo
            echo "(internal $SRC)"
        } | git commit-tree -p "$NEW" "$TREE"
    )
done

git push "$PUBLIC_REMOTE" "$NEW:refs/heads/$BRANCH"  # new branch
#git push --force-with-lease "$PUBLIC_REMOTE" "$NEW:refs/heads/$BRANCH"
