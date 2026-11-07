// Contrast Checker Utility
// =============================================================================
// WCAG Contrast Ratio Checker
// Following WCAG 2.1 guidelines: AA = 4.5:1 (normal text), 3:1 (large text)
// AAA = 7:1 (normal text), 4.5:1 (large text)
// =============================================================================

export interface ColorRGB {
  r: number
  g: number
  b: number
}

export interface ContrastResult {
  ratio: number
  aa: boolean
  aaLarge: boolean
  aaa: boolean
  aaaLarge: boolean
}

/**
 * Convert hex color to RGB
 */
export function hexToRgb(hex: string): ColorRGB | null {
  // Remove # if present
  const cleanHex = hex.replace('#', '')

  // Handle 3-digit hex
  if (cleanHex.length === 3) {
    const r = parseInt(cleanHex[0] + cleanHex[0], 16)
    const g = parseInt(cleanHex[1] + cleanHex[1], 16)
    const b = parseInt(cleanHex[2] + cleanHex[2], 16)
    return { r, g, b }
  }

  // Handle 6-digit hex
  if (cleanHex.length === 6) {
    const r = parseInt(cleanHex.substring(0, 2), 16)
    const g = parseInt(cleanHex.substring(2, 4), 16)
    const b = parseInt(cleanHex.substring(4, 6), 16)
    return { r, g, b }
  }

  return null
}

/**
 * Convert RGB to relative luminance
 * Following WCAG 2.1 formula
 */
function getLuminance(rgb: ColorRGB): number {
  const { r, g, b } = rgb

  // Convert sRGB to linear RGB
  const toLinear = (channel: number): number => {
    const c = channel / 255
    return c <= 0.03928 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4)
  }

  const r = toLinear(rgb.r)
  const g = toLinear(rgb.g)
  const b = toLinear(rgb.b)

  // WCAG luminance formula
  return 0.2126 * r + 0.7152 * g + 0.0722 * b
}

/**
 * Calculate contrast ratio between two colors
 * Returns ratio from 1:1 to 21:1
 */
export function getContrastRatio(color1: string, color2: string): number {
  const rgb1 = hexToRgb(color1)
  const rgb2 = hexToRgb(color2)

  if (!rgb1 || !rgb2) {
    throw new Error('Invalid color format. Use hex format (#RRGGBB or #RGB)')
  }

  const lum1 = getLuminance(rgb1)
  const lum2 = getLuminance(rgb2)

  const brightest = Math.max(lum1, lum2)
  const darkest = Math.min(lum1, lum2)

  // Contrast ratio formula: (L1 + 0.05) / (L2 + 0.05)
  return (brightest + 0.05) / (darkest + 0.05)
}

/**
 * Check if contrast meets WCAG standards
 */
export function checkContrast(color1: string, color2: string): ContrastResult {
  const ratio = getContrastRatio(color1, color2)

  return {
    ratio: Math.round(ratio * 100) / 100,
    aa: ratio >= 4.5,
    aaLarge: ratio >= 3,
    aaa: ratio >= 7,
    aaaLarge: ratio >= 4.5,
  }
}

/**
 * Validate theme colors against WCAG standards
 */
export interface ThemeColors {
  text: string
  background: string
  primary?: string
  secondary?: string
  error?: string
  success?: string
  warning?: string
  border?: string
}

export interface ValidationResult {
  passed: boolean
  results: Record<string, ContrastResult>
  warnings: string[]
}

export function validateThemeContrast(theme: ThemeColors): ValidationResult {
  const results: Record<string, ContrastResult> = {}
  const warnings: string[] = []

  // Check text on background
  results.textOnBg = checkContrast(theme.text, theme.background)
  if (!results.textOnBg.aa) {
    warnings.push(`Text on background fails WCAG AA (${results.textOnBg.ratio}:1)`)
  }

  // Check primary on background
  if (theme.primary) {
    results.primaryOnBg = checkContrast(theme.primary, theme.background)
    if (!results.primaryOnBg.aaLarge) {
      warnings.push(`Primary on background fails WCAG AA Large (${results.primaryOnBg.ratio}:1)`)
    }
  }

  // Check secondary on background
  if (theme.secondary) {
    results.secondaryOnBg = checkContrast(theme.secondary, theme.background)
    if (!results.secondaryOnBg.aaLarge) {
      warnings.push(`Secondary on background fails WCAG AA Large (${results.secondaryOnBg.ratio}:1)`)
    }
  }

  // Check error on background
  if (theme.error) {
    results.errorOnBg = checkContrast(theme.error, theme.background)
    if (!results.errorOnBg.aa) {
      warnings.push(`Error on background fails WCAG AA (${results.errorOnBg.ratio}:1)`)
    }
  }

  // Check success on background
  if (theme.success) {
    results.successOnBg = checkContrast(theme.success, theme.background)
    if (!results.successOnBg.aa) {
      warnings.push(`Success on background fails WCAG AA (${results.successOnBg.ratio}:1)`)
    }
  }

  // Check warning on background
  if (theme.warning) {
    results.warningOnBg = checkContrast(theme.warning, theme.background)
    if (!results.warningOnBg.aa) {
      warnings.push(`Warning on background fails WCAG AA (${results.warningOnBg.ratio}:1)`)
    }
  }

  // Check border on background
  if (theme.border) {
    results.borderOnBg = checkContrast(theme.border, theme.background)
    if (!results.borderOnBg.aa) {
      warnings.push(`Border on background fails WCAG AA (${results.borderOnBg.ratio}:1)`)
    }
  }

  return {
    passed: warnings.length === 0,
    results,
    warnings,
  }
}

/**
 * Generate a contrast report for a theme
 */
export function generateContrastReport(theme: ThemeColors): string {
  const validation = validateThemeContrast(theme)
  const lines = [
    '=== WCAG Contrast Report ===',
    '',
    `Text on Background: ${validation.results.textOnBg.ratio}:1 ${validation.results.textOnBg.aa ? '✓ AA' : '✗ AA'} ${validation.results.textOnBg.aaa ? '✓ AAA' : ''}`,
  ]

  if (validation.results.primaryOnBg) {
    lines.push(`Primary on Background: ${validation.results.primaryOnBg.ratio}:1 ${validation.results.primaryOnBg.aa ? '✓ AA' : '✗ AA'} ${validation.results.primaryOnBg.aaa ? '✓ AAA' : ''}`)
  }

  if (validation.results.secondaryOnBg) {
    lines.push(`Secondary on Background: ${validation.results.secondaryOnBg.ratio}:1 ${validation.results.secondaryOnBg.aa ? '✓ AA' : '✗ AA'} ${validation.results.secondaryOnBg.aaa ? '✓ AAA' : ''}`)
  }

  if (validation.results.errorOnBg) {
    lines.push(`Error on Background: ${validation.results.errorOnBg.ratio}:1 ${validation.results.errorOnBg.aa ? '✓ AA' : '✗ AA'} ${validation.results.errorOnBg.aaa ? '✓ AAA' : ''}`)
  }

  if (validation.results.successOnBg) {
    lines.push(`Success on Background: ${validation.results.successOnBg.ratio}:1 ${validation.results.successOnBg.aa ? '✓ AA' : '✗ AA'} ${validation.results.successOnBg.aaa ? '✓ AAA' : ''}`)
  }

  if (validation.results.warningOnBg) {
    lines.push(`Warning on Background: ${validation.results.warningOnBg.ratio}:1 ${validation.results.warningOnBg.aa ? '✓ AA' : '✗ AA'} ${validation.results.warningOnBg.aaa ? '✓ AAA' : ''}`)
  }

  if (validation.results.borderOnBg) {
    lines.push(`Border on Background: ${validation.results.borderOnBg.ratio}:1 ${validation.results.borderOnBg.aa ? '✓ AA' : '✗ AA'} ${validation.results.borderOnBg.aaa ? '✓ AAA' : ''}`)
  }

  lines.push('')
  lines.push(`Overall: ${validation.passed ? 'PASSED ✓' : 'FAILED ✗'}`)

  if (validation.warnings.length > 0) {
    lines.push('')
    lines.push('Warnings:')
    validation.warnings.forEach(w => lines.push(`  - ${w}`))
  }

  return lines.join('\n')
}

/**
 * Test current theme against WCAG
 */
export function validateCurrentTheme(): ValidationResult {
  const rootStyles = getComputedStyle(document.documentElement)

  const theme: ThemeColors = {
    text: rootStyles.getPropertyValue('--color-text-primary').trim() || '#0f172a',
    background: rootStyles.getPropertyValue('--color-bg-primary').trim() || '#ffffff',
    primary: rootStyles.getPropertyValue('--color-primary').trim() || '#3b82f6',
    secondary: rootStyles.getPropertyValue('--color-secondary').trim() || '#06b6d4',
    error: rootStyles.getPropertyValue('--color-error').trim() || '#ef4444',
    success: rootStyles.getPropertyValue('--color-success').trim() || '#22c55e',
    warning: rootStyles.getPropertyValue('--color-warning').trim() || '#f59e0b',
    border: rootStyles.getPropertyValue('--color-border-primary').trim() || '#e8e8e8',
  }

  return validateThemeContrast(theme)
}