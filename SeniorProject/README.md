Project Information:

This program was developed as a capstone project for the Cybersecurity program at Western Washington University. The 
project features a GUI that allows users to upload spreadsheets and generate personalized video clips. The focus of 
the project was building a reliable, user-friendly system that processes spreadsheet data into multimedia output while
ensuring secure file handling with the projects remote storage infrastructure.

<img width="775" height="453" alt="image" src="https://github.com/user-attachments/assets/1e93c026-9e4e-4081-b042-a5494e4ccc32" />

## Development setup - (Windows or Linux) - Pythons venv (https://docs.python.org/3/library/venv.html)
Note:
Requirements:
- [Python 3](https://www.python.org/downloads/)- version 3.11 or earlier
- [ImageMagick](https://imagemagick.org/script/download.php) 

Set up a virtual python environment to isolate this project's dependencies:
```sh
# Create a virtual env called ".venv"
python3 -m venv .venv  

# Run the right activation command for your system to enter the environment:
source .venv/bin/activate # Bash/Zsh (POSIX/Unix/Linux/Mac)
.venv\Scripts\Activate.ps1 # Windows PowerShell

# Run `deactivate` to exit the virtual environment later on
```
Install dependencies
```sh
pip install -r src/utilities/requirements.txt
```
