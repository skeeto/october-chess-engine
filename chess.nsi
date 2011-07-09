!include "MUI2.nsh"

Name "October Chess Engine"
Outfile "dist/october-chess-${VERSION}-installer.exe"
XPStyle on

!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

InstallDir "$PROGRAMFILES\October Chess"

!insertmacro MUI_LANGUAGE "English"

!define ADDREM "Software\Microsoft\Windows\CurrentVersion\Uninstall"

section
  SetShellVarContext all
  setOutPath $INSTDIR
  file dist/Chess.exe
  file dist/Chess-src*.zip
  file dist/javadoc.zip
  writeUninstaller $INSTDIR\uninstall.exe
  createShortCut "$SMPROGRAMS\October Chess.lnk" "$INSTDIR\Chess.exe"
  createShortCut "$DESKTOP\October Chess.lnk" "$INSTDIR\Chess.exe"
  WriteRegStr HKLM "${ADDREM}\OctoberChess" \
                   "DisplayName" "October Chess"
  WriteRegStr HKLM "${ADDREM}\OctoberChess" \
  	           "UninstallString" "$\"$INSTDIR\uninstall.exe$\""
  WriteRegStr HKLM "${ADDREM}\OctoberChess" \
                   "QuietUninstallString" "$\"$INSTDIR\uninstall.exe$\" /S"
sectionEnd

section "Uninstall"
  SetShellVarContext all
  delete $INSTDIR\uninstall.exe
  RMDir /r $INSTDIR
  delete "$SMPROGRAMS\October Chess.lnk"
  delete "$DESKTOP\October Chess.lnk"
  DeleteRegKey HKLM "${ADDREM}\OctoberChess"
sectionEnd
