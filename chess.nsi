!define VERSION "1.0.1"
!define INST "October Chess"

!include "MUI2.nsh"

Name "October Chess Engine"
Outfile "dist/october-chess-${VERSION}-installer.exe"
XPStyle on

!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

InstallDir "$PROGRAMFILES\${INST}"

!insertmacro MUI_LANGUAGE "English"

section
  SetShellVarContext all
  setOutPath $INSTDIR
  file dist/Chess.exe
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
  delete "$SMPROGRAMS\${INST}\October Chess.lnk"
  delete "$SMPROGRAMS\${INST}\Uninstall.lnk"
  RMDir "$SMPROGRAMS\${INST}"
  delete "$DESKTOP\October Chess.lnk"
sectionEnd
