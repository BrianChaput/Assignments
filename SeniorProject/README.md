Project Information:

This program was developed by Kieran D, Ryan H, and myself, as part of our final project for the cybersecurity program at WWU. The main program is a GUI that allows the user to upload a spreadsheet and create custom video clips that feature student interviews
and the new buildings on campus.

<img width="1283" height="746" alt="image" src="https://github.com/user-attachments/assets/4739f25c-53ed-4f22-ba6d-be9b79292db0" />


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
