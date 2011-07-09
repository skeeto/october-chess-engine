!define INST "October Chess"

!include "MUI2.nsh"

Name "October Chess Engine"
Outfile "dist/october-chess-${VERSION}-installer.exe"
XPStyle on

!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

InstallDir "$PROGRAMFILES\${INST}"

!insertmacro MUI_LANGUAGE "English"

section
  SetShellVarContext all
  setOutPath $INSTDIR
  file dist/Chess.exe
  file dist/Chess-src*.zip
  writeUninstaller $INSTDIR\uninstall.exe
  CreateDirectory "$SMPROGRAMS\${INST}"
  createShortCut "$SMPROGRAMS\${INST}\October Chess.lnk" "$INSTDIR\Chess.exe"
  createShortCut "$SMPROGRAMS\${INST}\Uninstall.lnk" "$INSTDIR\uninstall.exe"
  createShortCut "$DESKTOP\October Chess.lnk" "$INSTDIR\Chess.exe"
sectionEnd

section "Uninstall"
  SetShellVarContext all
  delete $INSTDIR\uninstall.exe
  RMDir /r $INSTDIR
  RMDir /r "$SMPROGRAMS\${INST}"
  delete "$DESKTOP\October Chess.lnk"
sectionEnd
