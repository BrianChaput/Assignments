# Guide: Moving Multiple Repositories into /Assignments

This guide explains how to consolidate multiple separate repositories into the `/Assignments` repository while preserving git history.

## Table of Contents
1. [Overview](#overview)
2. [Method 1: Git Subtree (Recommended)](#method-1-git-subtree-recommended)
3. [Method 2: Git Submodules](#method-2-git-submodules)
4. [Method 3: Manual Merge with History](#method-3-manual-merge-with-history)
5. [Method 4: Simple Copy (No History)](#method-4-simple-copy-no-history)

---

## Overview

There are several approaches to moving repositories, each with trade-offs:

- **Git Subtree**: Best for fully integrating repos while preserving history
- **Git Submodules**: Best for maintaining repos as separate entities
- **Manual Merge**: Best for custom control over the merge process
- **Simple Copy**: Fastest but loses git history

---

## Method 1: Git Subtree (Recommended)

This method merges repositories while preserving their complete git history.

### Prerequisites
- Local clone of the Assignments repository
- URLs or local paths of repositories to merge

### Steps

1. **Navigate to the Assignments repository:**
   ```bash
   cd /path/to/Assignments
   ```

2. **Add each repository as a remote:**
   ```bash
   git remote add -f <repo-name> <repo-url>
   ```
   Example:
   ```bash
   git remote add -f project1 https://github.com/user/project1.git
   git remote add -f project2 https://github.com/user/project2.git
   ```

3. **Merge each repository into a subdirectory:**
   ```bash
   git subtree add --prefix=<subdirectory> <repo-name> <branch> --squash
   ```
   Example:
   ```bash
   git subtree add --prefix=Project1 project1 main --squash
   git subtree add --prefix=Project2 project2 main --squash
   ```

   Note: Remove `--squash` if you want to preserve all individual commits (results in longer history).

4. **Clean up remotes (optional):**
   ```bash
   git remote remove <repo-name>
   ```

5. **Push the changes:**
   ```bash
   git push origin main
   ```

### Result Structure
```
Assignments/
├── Project1/
│   ├── (files from project1)
├── Project2/
│   ├── (files from project2)
└── README.md
```

---

## Method 2: Git Submodules

This method links repositories as submodules, keeping them as separate entities.

### Steps

1. **Navigate to the Assignments repository:**
   ```bash
   cd /path/to/Assignments
   ```

2. **Add each repository as a submodule:**
   ```bash
   git submodule add <repo-url> <subdirectory>
   ```
   Example:
   ```bash
   git submodule add https://github.com/user/project1.git Project1
   git submodule add https://github.com/user/project2.git Project2
   ```

3. **Commit the changes:**
   ```bash
   git commit -m "Add submodules for Project1 and Project2"
   git push origin main
   ```

4. **When cloning, others must initialize submodules:**
   ```bash
   git clone --recurse-submodules <assignments-repo-url>
   ```
   Or after cloning:
   ```bash
   git submodule update --init --recursive
   ```

### Pros and Cons
- ✅ Maintains separate repositories
- ✅ Easy to update individual projects
- ❌ More complex for collaborators
- ❌ Submodules point to specific commits

---

## Method 3: Manual Merge with History

This method gives you full control over merging repositories.

### Steps

1. **Navigate to the Assignments repository:**
   ```bash
   cd /path/to/Assignments
   ```

2. **For each repository, perform a merge:**
   ```bash
   # Add remote
   git remote add project1-remote <repo-url>
   git fetch project1-remote
   
   # Create a branch from the fetched repo
   git checkout -b project1-merge project1-remote/main
   
   # Move all files into subdirectory
   mkdir -p Project1
   git ls-files -z | xargs -0 -I {} git mv {} Project1/
   git commit -m "Move Project1 files to subdirectory"
   
   # Merge into main
   git checkout main
   git merge project1-merge --allow-unrelated-histories
   git commit -m "Finalize Project1 integration"
   
   # Clean up
   git branch -d project1-merge
   git remote remove project1-remote
   ```

4. **Repeat for each repository**

5. **Push changes:**
   ```bash
   git push origin main
   ```

---

## Method 4: Simple Copy (No History)

The fastest method but doesn't preserve git history.

### Steps

1. **Clone each repository locally:**
   ```bash
   cd /tmp
   git clone <repo-url> project1
   git clone <repo-url> project2
   ```

2. **Navigate to Assignments repository:**
   ```bash
   cd /path/to/Assignments
   ```

3. **Copy files (excluding .git):**
   ```bash
   mkdir -p Project1 Project2
   rsync -av --exclude='.git' /tmp/project1/ Project1/
   rsync -av --exclude='.git' /tmp/project2/ Project2/
   ```

4. **Commit and push:**
   ```bash
   git add .
   git commit -m "Add Project1 and Project2"
   git push origin main
   ```

---

## Best Practices

1. **Backup First**: Always create backups of original repositories before merging
2. **Clean Before Merging**: Remove unnecessary files (.DS_Store, node_modules, etc.)
3. **Update .gitignore**: Merge and deduplicate .gitignore files
4. **README Updates**: Update the main README to reflect the new structure
5. **Dependencies**: Consolidate package managers (package.json, requirements.txt, etc.)
6. **Test After Merge**: Ensure all projects still build and run correctly

## Recommended Workflow

For most use cases, we recommend **Method 1 (Git Subtree)** with the following workflow:

```bash
# 1. Navigate to Assignments
cd /path/to/Assignments

# 2. For each repository:
git remote add -f repo-name <url>
git subtree add --prefix=ProjectName repo-name main --squash

# 3. Clean up
git remote remove repo-name

# 4. Push
git push origin main
```

This preserves essential history while keeping the commit log clean with `--squash`.

---

## Troubleshooting

### Issue: Merge Conflicts
- Manually resolve conflicts in affected files
- Use `git status` to see conflicted files
- Edit, then `git add` and `git commit`

### Issue: Large History
- Use `--squash` with subtree to condense commits
- Consider using shallow clones: `git clone --depth=1`

### Issue: Binary Files or Large Files
- Use Git LFS before merging:
  ```bash
  # Install Git LFS if not already installed
  git lfs install
  
  # Track large file types
  git lfs track "*.pdf"
  git lfs track "*.zip"
  git lfs track "*.mp4"
  
  # Commit the .gitattributes file
  git add .gitattributes
  git commit -m "Configure Git LFS"
  ```
- Migrate existing large files to LFS:
  ```bash
  git lfs migrate import --include="*.pdf,*.zip" --everything
  ```

### Issue: Different Directory Structures
- Restructure before merging
- Use scripts to reorganize files if needed

---

## Additional Resources

- [Git Subtree Documentation](https://git-scm.com/book/en/v2/Git-Tools-Advanced-Merging)
- [Git Submodules Documentation](https://git-scm.com/book/en/v2/Git-Tools-Submodules)
- [Atlassian Git Subtree Tutorial](https://www.atlassian.com/git/tutorials/git-subtree)

---

## Questions?

If you encounter issues or need clarification, please open an issue in this repository.
